package com.cangsg.brick.kt.entry;

public final class KtEntryImpl<T> implements KtEntry {
	private Object key;
	private T value;

	public KtEntryImpl() {

	}

	public KtEntryImpl(Object key, T value) {
		this.key = key;
		this.value = value;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
