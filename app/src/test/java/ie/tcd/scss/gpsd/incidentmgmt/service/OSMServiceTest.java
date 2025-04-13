package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.model.osm.OSMGeoReverseResponseDTO;
import ie.tcd.scss.gpsd.incidentmgmt.webclient.osm.OSMWebClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for the OSMService.
 */
@ExtendWith(MockitoExtension.class)
public class OSMServiceTest {

    @Mock
    private OSMWebClient osmWebClient;

    @InjectMocks
    private OSMService osmService;

    @Test
    public void testGetReverseGeoCodeSuccess() {
        // Arrange: Define latitude, longitude, and the expected DTO.
        Double lat = 40.7128;
        Double lon = -74.0060;
        OSMGeoReverseResponseDTO expectedResponse = OSMGeoReverseResponseDTO.builder()
                .name("New York")
                .displayName("New York, NY, USA")
                .build();

        // Stub the call to the web client.
        when(osmWebClient.reverseGeoCode(lat, lon)).thenReturn(expectedResponse);

        // Act: Invoke the service method.
        OSMGeoReverseResponseDTO actualResponse = osmService.getReverseGeoCode(lat, lon);

        // Assert: Verify the response is as expected.
        assertNotNull(actualResponse, "Expected a non-null response");
        assertEquals("New York", actualResponse.getName(), "Expected the name to match");
        assertEquals("New York, NY, USA", actualResponse.getDisplayName(), "Expected the display name to match");

        // Verify that the web client method was called exactly once.
        verify(osmWebClient, times(1)).reverseGeoCode(lat, lon);
    }

    @Test
    public void testGetReverseGeoCodeThrowsException() {
        // Arrange: Define latitude and longitude.
        Double lat = 40.7128;
        Double lon = -74.0060;
        RuntimeException exception = new RuntimeException("Test Exception");

        // Stub the web client to throw an exception.
        when(osmWebClient.reverseGeoCode(lat, lon)).thenThrow(exception);

        // Act & Assert: Expect the same exception to be thrown from the service.
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            osmService.getReverseGeoCode(lat, lon);
        });
        assertEquals("Test Exception", thrown.getMessage(), "Expected exception message to match");
        verify(osmWebClient, times(1)).reverseGeoCode(lat, lon);
    }
}