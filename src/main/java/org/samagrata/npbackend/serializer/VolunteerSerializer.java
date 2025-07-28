package org.samagrata.npbackend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

import org.samagrata.npbackend.entity.VolunteerEntity;

public class VolunteerSerializer extends JsonSerializer<VolunteerEntity> {

  @Override
  public void serialize(
    VolunteerEntity entity,
    JsonGenerator gen,
    SerializerProvider serializers
  ) throws IOException {
    gen.writeStartObject();
    // gen.writeNumberField("id", entity.getId());
    // gen.writeStringField("customName", entity.getUsername().toUpperCase()); // Custom logic
    gen.writeEndObject();
  }
}
