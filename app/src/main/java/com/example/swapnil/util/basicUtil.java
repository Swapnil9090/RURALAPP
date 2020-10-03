public class basicUtil{
    public boolean isNull(Object obj){
        return obj == null;
    }
    
    public boolean notNull(Object obj){
        return obj != null;
    }
    
    public String blankIfNull(String str){
        return obj == null ? "" : str;
    }
}