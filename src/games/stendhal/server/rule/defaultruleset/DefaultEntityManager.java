/*
 * DefaultEntityManager.java
 *
 * Created on 19. August 2005, 21:44
 *
 */

package games.stendhal.server.rule.defaultruleset;

import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.creature.Creature;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.rule.EntityManager;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Matthias Totz
 */
public class DefaultEntityManager implements EntityManager
{
  /** the singleton instance, lazy initialisation */
  private static DefaultEntityManager manager;

  /** maps the tile ids to the classes */
  private Map<Integer, String> idToClass;

  /** maps the creature tile-ids to the actual creature enums */
  private Map<String, DefaultCreature> classToCreature;

  /** maps the item names to the actual item enums */
  private Map<String, DefaultItem> classToItem;

  /** no public constructor */
  private DefaultEntityManager()
  {
    idToClass = new HashMap<Integer, String>();
    // Build the creatures tables
    classToCreature = new HashMap<String,DefaultCreature>();
    DefaultCreature[] creatures = DefaultCreature.values();
    for (DefaultCreature creature : creatures )
    {
      int id = creature.getTileId();
      String clazz = creature.getCreatureClass();
      classToCreature.put(clazz, creature);
      if (id > 0)
      {
        idToClass.put(id, clazz);
      }
    }
    
    // Build the items tables
    classToItem = new HashMap<String,DefaultItem>();
    DefaultItem[] items = DefaultItem.values();
    for (DefaultItem item : items)
    {
      int id = item.getTileId();
      String clazz = item.getItemClass();
      classToItem.put(clazz, item);
      if (id > 0)
      {
        idToClass.put(id, clazz);
      }
    }
  }

  /** 
   * returns the instance of this manager.
   * Note: This method is synchonized.
   */
  public static synchronized DefaultEntityManager getInstance()
  {
    if (manager == null)
    {
      manager = new DefaultEntityManager();
    }
    return manager;
  }
  
  /** returns the entity or <code>null</code> if the id is unknown */
  public Entity getEntity(int id)
  {
    if (id < 0)
      return null;
    
    String clazz = idToClass.get(id);
    if (clazz == null)
      return null;

    
    return getEntity(clazz);
  }
  
  /** 
   * returns the entity or <code>null</code> if the id is unknown 
   * @throws NullPointerException if clazz is <code>null</code>
   */
  public Entity getEntity(String clazz)
  {
    if (clazz == null)
      throw new NullPointerException("entity class is null");
    
    Entity entity;
    // Lookup the id in the creature table
    entity = getCreature(clazz);
    if (entity != null)
      return entity;
    
    // Lookup the id in the item table
    entity = getItem(clazz);
    if (entity != null)
      return entity;
    
    return null;
  }

  /** 
   * returns the creature or <code>null</code> if the id is unknown 
   */
  public Creature getCreature(int id)
  {
    if (id < 0)
      return null;

    String clazz = idToClass.get(id);
    if (clazz == null)
      return null;

    return getCreature(clazz);
  }

  /** 
   * returns the creature or <code>null</code> if the clazz is unknown 
   * @throws NullPointerException if clazz is <code>null</code>
   */
  public Creature getCreature(String clazz)
  {
    if (clazz == null)
      throw new NullPointerException("entity class is null");
    
    // Lookup the clazz in the creature table
    DefaultCreature creature = classToCreature.get(clazz);
    if (creature != null)
      return  creature.getCreature();
    
    return null;
  }

  /** return true if the Entity is a creature */
  public boolean isCreature(int id)
  {
    if (id < 0)
      return false;

    String clazz = idToClass.get(id);
    if (clazz == null)
      return false;

    return isCreature(clazz);
  }

  /** return true if the Entity is a creature */
  public boolean isCreature(String clazz)
  {
    if (clazz == null)
      throw new NullPointerException("entity class is null");
    return classToCreature.containsKey(clazz);
  }

  /** 
   * returns the item or <code>null</code> if the id is unknown 
   */
  public Item getItem(int id)
  {
    if (id < 0)
      return null;

    String clazz = idToClass.get(id);
    if (clazz == null)
      return null;

    return getItem(clazz);
  }

  /** 
   * returns the item or <code>null</code> if the clazz is unknown 
   * @throws NullPointerException if clazz is <code>null</code>
   */
  public Item getItem(String clazz)
  {
    if (clazz == null)
      throw new NullPointerException("entity class is null");
    
    // Lookup the clazz in the item table
    DefaultItem item = classToItem.get(clazz);
    if (item != null)
      return  item.getItem();
    
    return null;
  }
}
