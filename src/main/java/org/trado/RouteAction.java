package org.trado;

class RouteAction<T> {
    private final T action;
    private final boolean containsWildCard;
    private int wildCardPosition;
    private String wildCardKey;

    RouteAction(T action){
        this(action, false);
    }

    RouteAction(T action, boolean containsWildCard) {
        this.action = action;
        this.containsWildCard = containsWildCard;
    }

    public T action() {
        return action;
    }

    public boolean containsWildCard() {
        return containsWildCard;
    }

    public int wildCardPosition() {
        return wildCardPosition;
    }

    public void wildCardPosition(int pos) {
        this.wildCardPosition = pos;
    }

    public String wildCardKey() {
        return wildCardKey;
    }

    public void wildCardKey(String wildCardKey) {
        this.wildCardKey = wildCardKey;
    }
}
