package wc;


public class Application {

    public static void main(String[] args){

       String command = null;

       String source = null;

       if(args.length == 1){
           if(args[0].startsWith("-")){
            command = args[0];
            source = "Stdin";
           } else {
               command = "-all";
               source = args[0];
           }
       } else if(args.length == 2){
           command = args[0];
           source = args[1];
       } else {
           System.out.println("Usage: wc <command> <source>. Std input is the default source");
           System.exit(1);
       }

       Commander.execute(command, source);
    }
}