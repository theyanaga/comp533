package com.theyanaga.observers;

import com.theyanaga.helpers.Action;
import com.theyanaga.helpers.Methods;

public class PropertyChange { 
	String callerName;
	Methods methodName;
	Action action;

public PropertyChange(String aCallerName, Methods aMethodName, Action anAction) {
	callerName = aCallerName;
	methodName = aMethodName;
	action = anAction;
}

public String callerName() {
	return callerName;
}

public Methods methodName() {
	return methodName;
}

public Action action() {
	return action;
}


}
