/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package qa.qcri.aidr.predictui.facade.imp;

import qa.qcri.aidr.predictui.facade.*;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import qa.qcri.aidr.predictui.entities.NominalAttribute;

/**
 *
 * @author Imran
 */
@Stateless
public class NominalAttributeFacadeImp implements NominalAttributeFacade {

    @PersistenceContext(unitName = "qa.qcri.aidr.predictui-EJBS")
    private EntityManager em;

    public List<NominalAttribute> getAllAttributes() {
        Query query = em.createNamedQuery("NominalAttribute.findAll", NominalAttribute.class);
        List<NominalAttribute> attributesList = query.getResultList();
        return attributesList;

    }

    public NominalAttribute addAttribute(NominalAttribute attribute) {
        em.persist(attribute);
        return attribute;
    }

    public NominalAttribute editAttribute(NominalAttribute attribute) {
        if (attribute == null) {
            throw new RuntimeException("Missing data values");
        }
        NominalAttribute attFound = em.find(NominalAttribute.class, attribute.getNominalAttributeID());
        if (attFound != null) {
            em.merge(attribute);
            return attribute;
        } else {
            throw new RuntimeException("Can't edit");
        }

    }

    public List<NominalAttribute> getAllAttributesbyByCollection(int crisisID) {
        return null;
    }
}
