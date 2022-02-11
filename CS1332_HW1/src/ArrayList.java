public class ArrayList<T> {

    public static final int INITIAL_CAPACITY = 9;
    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    public void addToBack(T data) {
        checkArrayCapacity();
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        backingArray[size] = data;
        size++;
    }

    public void addToFront(T data) {
        for(int i = size - 1; i >= 0; i--) {
            backingArray[i + 1] = backingArray[i];
        }
        backingArray[0] = data;
        size++;
    }
    
    public void addAtIndex(int index, T data) {
        if (index == size - 1) {
            addToBack(data);
        } else if (index == 0) {
            addToFront(data);
        } else {
            for (int i = size - 1; i >= index; i--) {
                backingArray[i + 1] = backingArray[i];
            }
            backingArray[index] = data;
            size++;
        }
    }

    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();

        list.addAtIndex(0, "2a");   // 2a
        list.addAtIndex(0, "1a");   // 1a, 2a
        list.addAtIndex(2, "4a");   // 1a, 2a, 4a
        list.addAtIndex(2, "3a");   // 1a, 2a, 3a, 4a
        list.addAtIndex(0, "0a");   // 0a, 1a, 2a, 3a, 4a

        System.out.println(list);
    }
}