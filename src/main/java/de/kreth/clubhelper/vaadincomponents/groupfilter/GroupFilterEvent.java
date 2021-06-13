package de.kreth.clubhelper.vaadincomponents.groupfilter;

import java.util.Collections;
import java.util.List;

import de.kreth.clubhelper.data.GroupDef;

public class GroupFilterEvent {

    private final List<GroupDef> filteredGroups;

    GroupFilterEvent(List<GroupDef> filteredGroups) {
	this.filteredGroups = filteredGroups;
    }

    /**
     * 
     * 
     * @return unmodifiable List of Groups currently filtered.
     */
    public List<GroupDef> getFilteredGroups() {
	return Collections.unmodifiableList(filteredGroups);
    }
}
