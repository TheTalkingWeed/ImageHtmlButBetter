import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlUtils {


    public void HtmlUtils(){}
    public  List<Path> getDirs(Path path){

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isDirectory)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;

    }
    public  List<Path> findByFileExtension(Path path) {

        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory!");
        }

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString()
                            .endsWith(".jpg") || p.getFileName().toString()
                            .endsWith(".png") || p.getFileName().toString()
                            .endsWith(".jpeg") || p.getFileName().toString()
                            .endsWith(".bmp") || p.getFileName().toString()
                            .endsWith(".html"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public  void makeImgHtml(Path picPath,boolean hasnext,boolean hasprev,int depth,Path... morePath){
        Path temp = getHtmlPath(picPath);
        File f = new File(String.valueOf(temp.toFile()));
        String arrowBack;
        String arrowForward;
        Path next;
        Path prev;

        if (morePath.length == 2){
            prev = getHtmlPath(morePath[1]);
            next = getHtmlPath(morePath[0]);
            System.out.println(prev + "-------ez van ");
            System.out.println(picPath + "-------ennekasdf");
            arrowBack = "<a href= \" ./"+prev.toFile().getName()+"\"><<</a>";
            arrowForward ="<a href= \" ./"+next.toFile().getName()+"\">>></a>";


        }else{
             arrowForward = hasnext ? "<a href= \"./"+getHtmlPath(morePath[0]).toFile().getName()+"\">>></a>" : "<a>>></a>";
            arrowBack = hasprev ? "<a href= \" ./"+getHtmlPath(morePath[0]).toFile().getName()+"\"><<</a>" : "<a><<</a>";
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            String backDepth = "../".repeat(depth-1) + "index.html";
            bw.write(

               " <!DOCTYPE html>\n"+
                   " <html lang=\"en\">\n"+
                   " <head>\n"+
                   "     <meta charset=\"UTF-8\">\n"+
                   "     <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"+
                   "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"+
                   "     <title>"+ f.getName() +"</title>\n"+
                   " </head>\n"+
                   " <body>\n"+
                   "    <h1><a href=\""+backDepth+"\">Start Page</a></h1>\n"+
                   "    <h2><a href=\"./index.html\">Back</a></h2>\n"+
                   arrowBack + "\n" +
                   picPath.getFileName()+"\n" +
                   arrowForward +"\n" +
                   arrowForward.substring(0,arrowForward.length()-6) + "\n" +
                   "   <br><img src = \"" + "./" + picPath.toFile().getName() +"\"style=\"width:300px;\">\n" +
                   " </body>\n"+
                   " </html>\n"

            );
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public  void makeIndexHtml(Path path,int depth){
        File index = new File((path.toFile()) + "/index.html");
        File[] files = path.toFile().listFiles();

        StringBuilder dirs = new StringBuilder();
        dirs.append("<ul>\n");
        assert files != null;
        for (File value : files) {
            if (value.isDirectory())
                dirs.append("<li><a href=\"")
                    .append(value.getName())
                    .append("/index.html\">")
                    .append(value.getName())
                    .append("</a></li>\n");
        }
        dirs.append("</ul>\n");

        StringBuilder htmlFiles = new StringBuilder();
        htmlFiles.append("<ul>\n");
        for (File file : files) {
            if (isHtml(file) && !file.getName().equals("index.html"))
                htmlFiles.append("<li><a href=\"")
                         .append(file.getName())
                         .append("\">")
                         .append(file.getName().replace(".html",".jpg"))
                         .append("</a></li>\n");
        }
        htmlFiles.append("</ul>\n");

        String backLink = depth == 0 ? "":"<h2><a href = \"../index.html\">Back</a></h2>\n";

        String backDepth = "../".repeat(depth) + "index.html";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(index));

            bw.write(

                "<html lang=\"en\">\n"+
                "<head>\n"+
                "  <meta charset=\"UTF-8\">\n"+
                "  <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n"+
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"+
                "  <title>Document</title>\n"+
                "</head>\n"+
                "<body>\n"+
                "    <h1><a href=\""+backDepth+"\">Start Page</a></h1>\n"+
                backLink +
                "  <h1>Directories</h1>\n"+
                dirs+
                "  <h1>Images</h1>\n"+
                htmlFiles +
                "</body>\n"+
                "</html>\n"

        );

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private  Path getHtmlPath(Path path){
        return path.resolveSibling(path.getFileName().toString()
                .substring(0,path.getFileName().toString().lastIndexOf(".")) + ".html");
    }

    public  void deleteHTML(Path path){
        if (isHtml(path.toFile())){
            File f = new File(path.toString());
            f.delete();
        }
    }

    public  boolean isHtml(File file){
        String str= file.toString();
        return str.contains(".html");
    }


    public  int getDepth(Path p1,Path p2){
        int result = 0;

        String[] p1splitted = p1.toString().split("\\\\");
        String[] p2splitted = p2.toString().split("\\\\");

        result = p2splitted.length - p1splitted.length;

        return result;
    }
}
