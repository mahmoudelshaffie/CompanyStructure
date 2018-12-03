package org.tradeshit.companystructure.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@NamedEntityGraph(name = "graph.node.children", attributeNodes = @NamedAttributeNode(value = "children", subgraph = "children"))
public class Position {

	@Id
	private String key;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent")
	private Position parent;
	private String root;
	private int height = 0;

	@OneToMany
	@JoinColumn(name = "parent")
	private Set<Position> children = new HashSet<>();

	public Position() {
		this(null, null, null, 0);
	}

	public Position(String key) {
		this(key, null, null, 0);
	}

	public Position(String key, Position parent, String root, int height) {
		super();
		this.key = key;
		this.parent = parent;
		this.root = root;
		this.height = height;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Position getParent() {
		return parent;
	}

	public void setParent(Position parent) {
		this.parent = parent;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Set<Position> getChildren() {
		return children;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;

		if (Position.class.isInstance(obj)) {
			equals = key.equals(((Position) obj).getKey());
		}

		return equals;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	public boolean removeChild(Position child) {
		return this.children.remove(child);
	}

	public boolean addChild(Position child) {
		return children.add(child);
	}

	public boolean isChild(Position child) {
		return children.contains(child);
	}
}
