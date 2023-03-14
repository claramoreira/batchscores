package br.com.claramoreira.batchscores.batchscores.reader;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.claramoreira.batchscores.batchscores.model.Wine;

public class WineReader implements ItemReader<Wine>{

	
	private final String apiUrl;
    private final RestTemplate restTemplate;
 
    private int nextWineIndex;
    private List<Wine> wineData;
    
    public WineReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        nextWineIndex = 0;
    }

	@Override
    public Wine read() throws Exception {
        if (wineDataIsNotInitialized()) {
            wineData = fetchWineDataFromAPI();
        }
 
        Wine nextWine= null;
 
        if (nextWineIndex < wineData.size()) {
            nextWine = wineData.get(nextWineIndex);
            nextWineIndex++;
        }
        else {
        	nextWineIndex = 0;
        	wineData = null;
        }
 
        return nextWine;
    }
 
    private boolean wineDataIsNotInitialized() {
        return this.wineData == null;
    }
 
    private List<Wine> fetchWineDataFromAPI() {
        ResponseEntity<Wine[]> response = restTemplate.getForEntity(apiUrl,
                Wine[].class
        );
        Wine[] wineData = response.getBody();
        return Arrays.asList(wineData);
    }

}
