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

        HtmlUtils hmut = new HtmlUtils();

        Path path = Paths.get(args[0]);
        List<Path> dirs = new ArrayList<>(hmut.getDirs(path));

        List<Path> paths = hmut.findByFileExtension(path);

        paths.forEach(hmut::deleteHTML);

        boolean hasPrev;
        boolean hasNext;
        int depth = 0;
        for (Path p:paths) {
            if (!hmut.isHtml(p.toFile())) {
                System.out.println(p);
                hasPrev = paths.indexOf(p) != 0 && paths.get(paths.indexOf(p) - 1).getParent().equals(p.getParent());
                hasNext = paths.indexOf(p) != (paths.size() - 1) && paths.get(paths.indexOf(p) + 1).getParent().equals(p.getParent());

                depth = hmut.getDepth(dirs.get(0), p);
                if (hasPrev && hasNext) {
                    hmut.makeImgHtml(p, true, true, depth, paths.get(paths.indexOf(p) + 1), paths.get(paths.indexOf(p) - 1));
                } else if (hasNext) {
                    hmut.makeImgHtml(p, true, false, depth, paths.get(paths.indexOf(p) + 1));
                } else if (hasPrev) {
                    hmut.makeImgHtml(p, false, true, depth, paths.get(paths.indexOf(p) - 1));
                }

            }
        }

        for (Path p:dirs){
            hmut.makeIndexHtml(p,hmut.getDepth(dirs.get(0),p));
        }




        System.out.println("Program finished");
    }
}
