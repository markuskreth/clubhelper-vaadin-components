package de.kreth.clubhelper.vaadincomponents.validators;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;
import com.vaadin.flow.data.validator.EmailValidator;

import de.kreth.clubhelper.data.Contact;
import de.kreth.clubhelper.data.ContactType;

public class ContactValidator extends AbstractValidator<Contact> {

    private static final long serialVersionUID = -2053973887929125747L;
    EmailValidator emailValidator = new EmailValidator("Die E-Mail Adresse ist nicht gültig.");
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public ContactValidator() {
	super("");
    }

    @Override
    public ValidationResult apply(Contact value, ValueContext context) {

	if (ContactType.EMAIL.equals(value.getType())) {
	    return emailValidator.apply(value.getValue(), context);
	}
	if (ContactType.MOBILE.equals(value.getType())) {
	    return validateMobile(value.getValue());
	}
	return validateTelephone(value.getValue());
    }

    private ValidationResult validateMobile(String value) {

	try {
	    PhoneNumber number = phoneNumberUtil.parse(value, "DE");
	    if (phoneNumberUtil.isValidNumber(number) && startsWithGermanMobilePrefix(number)) {
		return ValidationResult.ok();
	    }

	    return ValidationResult.create("Die Handynummer scheint ungültig zu sein.",
		    ErrorLevel.WARNING);
	} catch (NumberParseException e) {
	    return ValidationResult.create("Fehler bei der Auswertung der Telefonnummer: " + e.getLocalizedMessage(),
		    ErrorLevel.WARNING);
	}

    }

    private boolean startsWithGermanMobilePrefix(PhoneNumber number) {
	String nationalNumber = String.valueOf(number.getNationalNumber());
	return nationalNumber.startsWith("17") || nationalNumber.startsWith("16") || nationalNumber.startsWith("15");
    }

    private ValidationResult validateTelephone(String value) {
	try {
	    PhoneNumber number = phoneNumberUtil.parse(value, "DE");
	    if (phoneNumberUtil.isValidNumber(number)) {
		return ValidationResult.ok();
	    }
	    return ValidationResult.create("Die Telefonnummer scheint ungültig zu sein.",
		    ErrorLevel.WARNING);
	} catch (NumberParseException e) {
	    return ValidationResult.create("Fehler bei der Auswertung der Telefonnummer: " + e.getLocalizedMessage(),
		    ErrorLevel.WARNING);
	}

    }

}
