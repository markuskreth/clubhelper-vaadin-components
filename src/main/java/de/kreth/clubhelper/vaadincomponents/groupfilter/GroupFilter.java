package de.kreth.clubhelper.vaadincomponents.groupfilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.HasValue.ValueChangeListener;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;

import de.kreth.clubhelper.data.GroupDef;

public class GroupFilter extends FlexLayout {

	private static final long serialVersionUID = -4592360743318501880L;

	private final List<GroupFilterListener> listeners = new ArrayList<>();
	private final List<Checkbox> checkboxes = new ArrayList<>();
	private final CheckboxChangeListener innerListener = new CheckboxChangeListener();

	private Map<String, GroupDef> allGroups = new HashMap<>();

	public GroupFilter(List<GroupDef> allGroups) {
		
		setFlexDirection(FlexDirection.ROW);
		setAlignItems(Alignment.START);
		setFlexWrap(FlexWrap.WRAP);
		setAlignContent(ContentAlignment.START);
		
		List<String> selected = getStoredIds();

		for (GroupDef groupDef : allGroups) {
			String id = "group_" + groupDef.getId();
			Checkbox checkbox = new Checkbox(selected == null || selected.contains(id));
			checkbox.setLabel(groupDef.getName());
			checkbox.setId(id);
			checkbox.addValueChangeListener(innerListener);
			add(checkbox);
			checkboxes.add(checkbox);
			this.allGroups.put(id, groupDef);
		}
	}

	private List<String> getStoredIds() {
		Cookie[] cookies = VaadinRequest.getCurrent().getCookies();

		List<String> selected = null;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(GroupFilter.class.getName())) {
				selected = Arrays.asList(cookie.getValue().split("\\|"));
				if (selected != null && selected.isEmpty()) {
					selected = null;
				}
				break;
			}
		}
		return selected;
	}

	private void storeIds(List<String> selectedIds) {
		Cookie cookie = new Cookie(GroupFilter.class.getName(), String.join("|", selectedIds));
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		VaadinService.getCurrentResponse().addCookie(cookie);
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
			List<String> selectedIds = new ArrayList<>();
			for (Checkbox checkbox : checkboxes) {
				if (Boolean.TRUE.equals(checkbox.getValue())) {
					selectedIds.add(checkbox.getId().get());
					selectedGroups.add(allGroups.get(checkbox.getId().get()));
				}
			}
			storeIds(selectedIds);
			GroupFilterEvent ev = new GroupFilterEvent(selectedGroups);
			for (GroupFilterListener groupFilterListener : filterListeners) {
				groupFilterListener.groupFilterChange(ev);
			}
		}

	}
}
