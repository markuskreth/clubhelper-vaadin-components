package de.kreth.clubhelper.vaadincomponents.groupfilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import de.kreth.clubhelper.data.GroupDef;

public class GroupFilter extends HorizontalLayout {

    private static final long serialVersionUID = -4592360743318501880L;

    private final List<GroupFilterListener> listeners = new ArrayList<>();
    private final List<Checkbox> checkboxes = new ArrayList<>();
    private final CheckboxChangeListener innerListener = new CheckboxChangeListener();

    private Map<String, GroupDef> allGroups = new HashMap<>();

    public GroupFilter(List<GroupDef> allGroups) {
	for (GroupDef groupDef : allGroups) {
	    Checkbox checkbox = new Checkbox(true);
	    checkbox.setLabel(groupDef.getName());
	    checkbox.setId("group_" + groupDef.getId());
	    checkbox.addValueChangeListener(innerListener);
	    add(checkbox);
	    checkboxes.add(checkbox);
	    this.allGroups.put(checkbox.getId().get(), groupDef);
	}
    }

    public void addListener(GroupFilterListener listener) {
	listeners.add(listener);
	innerListener.fireEvent(listener);
    }

    public void removeListener(GroupFilterListener listener) {
	listeners.remove(listener);
    }

    private class CheckboxChangeListener implements ValueChangeListener<ComponentValueChangeEvent<Checkbox, Boolean>> {

	private static final long serialVersionUID = 8802586065649984654L;

	@Override
	public void valueChanged(ComponentValueChangeEvent<Checkbox, Boolean> event) {
	    fireEvent(listeners.toArray(new GroupFilterListener[0]));
	}

	void fireEvent(GroupFilterListener... filterListeners) {
	    List<GroupDef> selectedGroups = new ArrayList<>();
	    for (Checkbox checkbox : checkboxes) {
		if (Boolean.TRUE.equals(checkbox.getValue())) {
		    selectedGroups.add(allGroups.get(checkbox.getId().get()));
		}
	    }
	    GroupFilterEvent ev = new GroupFilterEvent(selectedGroups);
	    for (GroupFilterListener groupFilterListener : filterListeners) {
		groupFilterListener.groupFilterChange(ev);
	    }
	}

    }
}
