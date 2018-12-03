package org.tradeshit.companystructure.rest.json;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.boot.jackson.JsonComponent;
import org.tradeshit.companystructure.entities.Position;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class PositionJsonComponent {
	
	private static String KEY_FIELD = "key";
	private static String HEIGHT_FIELD = "height";
	private static String PARENT_FIELD = "parent";
	private static String ROOT_FIELD = "root";
	private static String CHILDREN_FIELD = "children";
	
	public static class PositionJsonSerializer extends JsonSerializer<Position> {

		@Override
		public void serialize(Position position, JsonGenerator gen, SerializerProvider provider) throws IOException {
			gen.writeStartObject();
			gen.writeStringField(KEY_FIELD, position.getKey());
			gen.writeNumberField(HEIGHT_FIELD, position.getHeight());
			if (position.getParent() != null) {
				gen.writeStringField(PARENT_FIELD, position.getParent().getKey());				
			} 
			
			if (position.getRoot() != null) {
				gen.writeStringField(ROOT_FIELD, position.getRoot());
			}
			
			if (position.getChildren() != null && !position.getChildren().isEmpty()) {
				gen.writeArrayFieldStart(CHILDREN_FIELD);
				Iterator<Position> it = position.getChildren().iterator();
				while (it.hasNext()) {
					Position child = it.next();
					serialize(child, gen, provider);
				}
				gen.writeEndArray();
			}
			gen.writeEndObject();
		}
		
	}
}
