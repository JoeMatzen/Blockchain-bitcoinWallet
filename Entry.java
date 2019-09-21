package sherCoin;

import java.util.Map;

final class Entry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    public Entry() {
        key = key;
        value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
    
    /*public K setKey(K key)
    {
    K old = this.key;
    this.key = key;
    return old;
    }*/
    
    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
