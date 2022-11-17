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
        int depth = 0;
        for (Path p:paths) {
            if (!HtmlUtils.isHtml(p.toFile())) {
                System.out.println(p);
                hasPrev = paths.indexOf(p) != 0 && paths.get(paths.indexOf(p) - 1).getParent().equals(p.getParent());
                hasNext = paths.indexOf(p) != (paths.size() - 1) && paths.get(paths.indexOf(p) + 1).getParent().equals(p.getParent());

                depth = HtmlUtils.getDepth(dirs.get(0), p);
                if (hasPrev && hasNext) {
                    HtmlUtils.makeImgHtml(p, true, true, depth, paths.get(paths.indexOf(p) + 1), paths.get(paths.indexOf(p) - 1));
                } else if (hasNext) {
                    HtmlUtils.makeImgHtml(p, true, false, depth, paths.get(paths.indexOf(p) + 1));
                } else if (hasPrev) {
                    HtmlUtils.makeImgHtml(p, false, true, depth, paths.get(paths.indexOf(p) - 1));
                }

            }
        }

        for (Path p:dirs){
            HtmlUtils.makeIndexHtml(p,HtmlUtils.getDepth(dirs.get(0),p));
        }




        System.out.println("Program finished");
    }
}