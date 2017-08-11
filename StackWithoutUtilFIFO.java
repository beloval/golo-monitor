
FIFO without util

public class Start{

    public static void main(String []args){
        Stack stack = new Stack();
        stack.push("start1");
        stack.push("start2");
        stack.push("start3");
        System.out.println(stack.pop());
        System.out.println(stack.pop());
        System.out.println(stack.pop());
    }

}
public class Element{
    private Element next = null;
    private Object object;
    public Element(Object obj){
        this.object = obj;
    };
    public void setElement(Element element) {
        next= element;
    }
    public Element getElement(){
        return next;
    }
    public Object getObject(){
        return object;
    }
}
public class Stack{
    Element currentElement = null;

    public void push(Object object) {
        Element nextElement = new Element(object);
        if (currentElement != null) {
            nextElement.setElement(currentElement);
        };
        currentElement =  nextElement;

    }
    public Object pop() {
        Object result = currentElement.getObject();
        currentElement = currentElement.getElement();
        return result;
    }
}