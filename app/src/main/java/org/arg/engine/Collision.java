package org.arg.engine;

import org.arg.model.Element;

public class Collision {
    private final Element one;
    private final Element two;

    public Collision(Element one, Element two) {
        this.one = one;
        this.two = two;

    }
    public Element getOne() {
        return one;
    }
    public Element getTwo() {
        return two;
    }

}
