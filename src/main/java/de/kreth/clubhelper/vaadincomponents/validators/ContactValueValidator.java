package de.kreth.clubhelper.vaadincomponents.validators;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.data.binder.ErrorLevel;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;
import com.vaadin.flow.data.validator.EmailValidator;

import de.kreth.clubhelper.data.ContactType;

public class ContactValueValidator extends AbstractValidator<String> {

    private static final long serialVersionUID = 1L;
    private EmailValidator emailValidator = new EmailValidator("Die E-Mail Adresse ist nicht gültig.");
    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final HasValue<?, ContactType> typeBox;

    public ContactValueValidator(HasValue<?, ContactType> typeBox) {
	super("");
	this.typeBox = typeBox;
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
	ContactType type = typeBox.getValue();
	if (ContactType.EMAIL.equals(type)) {
	    return emailValidator.apply(value, context);
	}
	if (ContactType.MOBILE.equals(type)) {
	    return validateMobile(value);
	}
	return validateTelephone(value);
    }

    private ValidationResult validateMobile(String value) {

	try {
	    PhoneNumber number = phoneNumberUtil.parse(value, "DE");
	    if (phoneNumberUtil.isValidNumber(number) && startsWithGermanMobilePrefix(number)) {
		return ValidationResult.ok();
	    }

	    return ValidationResult.create("Die Handynummer scheint ungültig zu sein.",
		    ErrorLevel.ERROR);
	} catch (NumberParseException e) {
	    return ValidationResult.create("Fehler bei der Auswertung der Telefonnummer: " + e.getLocalizedMessage(),
		    ErrorLevel.ERROR);
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
		    ErrorLevel.ERROR);
	} catch (NumberParseException e) {
	    return ValidationResult.create("Fehler bei der Auswertung der Telefonnummer: " + e.getLocalizedMessage(),
		    ErrorLevel.ERROR);
	}

    }

}
