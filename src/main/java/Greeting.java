public class Greeting {
    private String content;

    Greeting(String content)
    {
        this.content = content;
    }

    Greeting(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}