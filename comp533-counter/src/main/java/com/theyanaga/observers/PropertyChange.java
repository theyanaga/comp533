package com.theyanaga.observers;

import com.theyanaga.helpers.Action;
import com.theyanaga.helpers.Methods;

public record PropertyChange(String callerName, Methods methodName, Action action) {
}
