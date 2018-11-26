package uebung;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Person {
    
    private final String id;
    private String firstname;
    private String lastname;
    private boolean isChild;
    private List<Person> children;
    private List<Person> friends;
    private String residence;
    
    public Person(final String id, boolean isChild) {
        this.id = id;
        this.children = new ArrayList<>();
        this.friends = new ArrayList<>();
        this.isChild = isChild;
    }
    
    public String getResidence() {
        return this.residence;
    }
    
    public void setResidence(final String residence) {
        this.residence = residence;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(final String name) {
        this.firstname = name;
    }
    
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(final String name) {
        this.lastname = name;
    }
    
    public boolean hasChildren() {
        return !this.children.isEmpty();
    }
    
    public boolean hasFriends() {
        return !this.friends.isEmpty();
    }
    
    public void pushChild(Person child) {
        this.children.add(child);
    }
    
    public void pushFriend(Person friend) {
        this.friends.add(friend);
    }
    
    public List<Person> getFriends() {
        return this.friends;
    }
    
    public List<Person> getChildren() {
        return this.children;
    }
    
    public boolean isChild() {
        return this.isChild;
    }
    
    @Override
    public String toString() {
        return String.format("(%s) '%s - %s' has %s children an self he/she is %s and lives '%s'. friends: " + friends() + " childen: " + children(), this.id, this.firstname, this.lastname, this.children.size(), isChild() ? "a child" : "not a child", this.residence);
    }
    
    private String friends() {
        return this.friends.stream().map( p -> p.firstname + " " + p.lastname).collect(Collectors.joining(","));
    }
    
    private String children() {
        return this.children.stream().map( p -> p.firstname + " " + p.lastname).collect(Collectors.joining(","));
    }
    
}
