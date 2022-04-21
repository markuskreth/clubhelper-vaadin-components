package de.kreth.clubhelper.vaadincomponents.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;

import de.kreth.clubhelper.data.Contact;
import de.kreth.clubhelper.data.ContactType;

public class ContactValidatorTest {

    private ContactValidator validator;

    @BeforeEach
    public void initValidator() {
	validator = new ContactValidator();
    }

    @Test
    public void testNonGermanMobile() {
	Contact contact = new Contact();
	contact.setType(ContactType.MOBILE);
	contact.setValue("0567468792");
	ValidationResult result = validator.apply(contact, new ValueContext());
	assertNotNull(result);
	assertEquals(ErrorLevel.WARNING, result.getErrorLevel().orElse(null));
	assertFalse(result.getErrorMessage().isBlank(), "Ein Fehlertext sollte ausgegeben werden.");
    }

    @Test
    public void testNonGermanMobile2() {
	Contact contact = new Contact();
	contact.setType(ContactType.MOBILE);
	contact.setValue("0137468792");
	ValidationResult result = validator.apply(contact, new ValueContext());
	assertNotNull(result);
	assertEquals(ErrorLevel.WARNING, result.getErrorLevel().orElse(null));
	assertFalse(result.getErrorMessage().isBlank(), "Ein Fehlertext sollte ausgegeben werden.");
    }

    @Test
    public void test017GermanMobile() {
	Contact contact = new Contact();
	contact.setType(ContactType.MOBILE);
	contact.setValue("017465487923");
	ValidationResult result = validator.apply(contact, new ValueContext());
	assertTrue(result.getErrorLevel().isEmpty());
    }
}
