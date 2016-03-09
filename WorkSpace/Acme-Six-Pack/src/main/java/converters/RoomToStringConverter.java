package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Room;

@Component
@Transactional
public class RoomToStringConverter implements Converter<Room, String> {
	
	@Override
	public String convert(Room room) {
		String result;

		if (room == null)
			result = null;
		else
			result = String.valueOf(room.getId());

		return result;
	}

}