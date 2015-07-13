
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;





/**
 *
 * @author Victor Anuebunwa
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            Material material = new Material(new URI("http://fileocation.php"));
            material.setAuthor("Victor Anuebunwa");
            material.setTitle("Fake Title");
            material.setType(MaterialType.CD);
            material.setDateAdded(new Date());
            material.setKeywords(new String[]{"software","Victor","Anuebunwa","avon","design","XML"});
//            material = library.getMaterials().get(1);
//            material.setAuthor("Stefan Kluth");
//            material.setKeywords(new String[]{"software","Stefan","Kluth","ooad","design","Object Oriented"});
            EditMaterialDialog dialog = new EditMaterialDialog(material);
            dialog.setVisible(true);
            
            System.out.println(material.isExcluded());
            System.exit(0);
        } catch (URISyntaxException ex) {
            Logger.getLogger(TestClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
