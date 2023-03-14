package br.com.claramoreira.batchscores.batchscores.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import br.com.claramoreira.batchscores.batchscores.model.Wine;
import br.com.claramoreira.batchscores.batchscores.model.WineString;

@Component
@StepScope
@Qualifier("wineItemProcessor")
public class WineItemProcessor implements ItemProcessor<Wine, WineString> {

	@Override
	public WineString process(Wine item) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(item.getId());
		sb.append(";");
		sb.append(item.getTitle());
		sb.append(";");
		sb.append(item.getDescription());
		return new WineString(sb.toString());
	}

}
