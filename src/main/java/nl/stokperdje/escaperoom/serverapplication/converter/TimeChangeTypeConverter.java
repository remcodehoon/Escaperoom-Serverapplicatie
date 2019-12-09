package nl.stokperdje.escaperoom.serverapplication.converter;

import nl.stokperdje.escaperoom.serverapplication.statics.pinslot.TimeChangeType;

import java.beans.PropertyEditorSupport;

public class TimeChangeTypeConverter extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(TimeChangeType.fromValue(text));
    }

}
