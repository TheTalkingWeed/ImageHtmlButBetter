import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length!=1){
            System.out.println("Hibás parancssori argumentumok!");
            System.exit(1);
        }

        Path path = Paths.get(args[0]);
        List<Path> dirs = new ArrayList<>(HtmlUtils.getDirs(path));

        List<Path> paths = HtmlUtils.findByFileExtension(path);

        paths.forEach(HtmlUtils::deleteHTML);

        boolean hasPrev;
        boolean hasNext;

        for (Path p:paths) {
            System.out.println(p);
            hasPrev = paths.indexOf(p) != 0 && paths.get(paths.indexOf(p) - 1).getParent().equals(p.getParent());
            hasNext = paths.indexOf(p) != (paths.size() -1) && paths.get(paths.indexOf(p) + 1).getParent().equals(p.getParent());


            if (hasPrev && hasNext){
                HtmlUtils.makeImgHtml(p,true,true,paths.get(paths.indexOf(p) - 1),paths.get(paths.indexOf(p) + 1));
            } else if (hasPrev){
                HtmlUtils.makeImgHtml(p,true,false,paths.get(paths.indexOf(p) - 1));
            } else if (hasNext){
                HtmlUtils.makeImgHtml(p,false,true,paths.get(paths.indexOf(p) + 1));
            }

        }

        dirs.forEach(HtmlUtils::makeIndexHtml);




        System.out.println("Program finished");
    }
}