function checkThisField(fieldName)
{
	if (fieldName.options[0].selected) {
		for (i = 1; i < fieldName.length; i++) {
			fieldName.options[i].selected = false;
		}
	}
	else {
		fieldName.options[0].selected = false;
	}

	if (fieldName.selectedIndex == -1) {
		fieldName.options[0].selected = true;
	}
}

function validateSelectFields()
{
	form = document.form1;
	
	for (i = 0; i < form.length; i++) {
		if ((form.elements[i].type == "select-multiple") && (form.elements[i].selectedIndex == -1)) {
			form.elements[i].options[0].selected = true;
		}
	}

	return false;
}