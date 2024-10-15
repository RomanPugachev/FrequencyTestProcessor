package org.example.frequencytestsprocessor.widgetsDecoration;

public abstract class WidgetDecorator<T> {
    protected T widget;
    public WidgetDecorator(T widget){
        this.widget = widget;
    }

    public T getWidget() {
        return widget;
    }
}
