/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.Collection;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.PersistenceException;
import qa.qcri.aidr.predictui.entities.Crisis;
import qa.qcri.aidr.predictui.entities.ModelFamily;

/**
 *
 * @author Imran
 */
@Local
public interface CrisisResourceFacade {
    
   public Crisis addCrisis(Crisis crisis) throws PersistenceException; 
   
   public Crisis editCrisis(Crisis crisis); 
   
   public Crisis getCrisisByID(int id);
   
   public boolean isCrisisExists(String crisisCode);
   
   public List<Crisis> getAllCrisis(); 
   
   public List<Crisis> getAllCrisisByUserID(int userID); 
   
}
