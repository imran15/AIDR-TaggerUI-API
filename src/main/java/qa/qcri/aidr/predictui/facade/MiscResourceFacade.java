/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade;

import java.util.List;
import javax.ejb.Local;
import javax.persistence.PersistenceException;
import qa.qcri.aidr.predictui.dto.ItemToLabelDTO;
import qa.qcri.aidr.predictui.dto.TrainingDataDTO;

/**
 *
 * @author Imran
 */
@Local
public interface MiscResourceFacade {
    
   public List<TrainingDataDTO> getTraningDataByCrisisAndAttribute(int crisisID, int modelFamilyID, int fromRecord, int limit);
   public ItemToLabelDTO getItemToLabel(int crisisID, int attributeID);
   
}
