package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Bulletin;

@Component
@Transactional
public class BulletinToStringConverter implements Converter<Bulletin, String> {
	
	@Override
	public String convert(Bulletin bulletin) {
		String result;

		if (bulletin == null)
			result = null;
		else
			result = String.valueOf(bulletin.getId());

		return result;
	}

}