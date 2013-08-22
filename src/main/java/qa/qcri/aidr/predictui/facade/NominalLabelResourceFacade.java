/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import qa.qcri.aidr.predictui.entities.NominalLabel;


/**
 *
 * @author Imran
 */
@Local
public interface NominalLabelResourceFacade {
    
   public NominalLabel addNominalLabel(NominalLabel label); 
   
   public NominalLabel editNominalLabel(NominalLabel label); 
   
   public NominalLabel getNominalLabelByID(int id);
   public List<NominalLabel> getAllNominalLabel(); 
   
    
}
