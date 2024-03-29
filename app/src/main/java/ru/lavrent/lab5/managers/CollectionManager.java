package ru.lavrent.lab5.managers;

import org.xml.sax.SAXException;
import ru.lavrent.lab5.exceptions.SerializationException;
import ru.lavrent.lab5.models.LabWork;
import ru.lavrent.lab5.util.XMLDumper;

import javax.xml.parsers.ParserConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * manages the collection of entities
 */
public class CollectionManager {
  private TreeSet<LabWork> collection;
  private String type;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private long lastId;
  private boolean hasUnsavedChanges;

  public CollectionManager() {
    hasUnsavedChanges = false;
    collection = new TreeSet<>();
    type = "TreeSet";
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.from(createdAt);
    lastId = 1;
  }

  /**
   * edit information about collection
   * 
   * @param type      type of storage the collection usees
   * @param createdAt date of creation
   * @param updatedAt date of update
   * @param lastId    last taken id + 1 (first free id)
   */
  public void setMetaData(String type, LocalDateTime createdAt, LocalDateTime updatedAt, long lastId) {
    this.type = type;
    this.createdAt = createdAt;
    this.lastId = lastId;
    this.updatedAt = updatedAt;
  }

  public long getCollectionSize() {
    return collection.size();
  }

  public String getType() {
    return type;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public long generateId() {
    return lastId++;
  }

  private void setUpdatedAt() {
    hasUnsavedChanges = true;
    updatedAt = LocalDateTime.now();
  }

  public void saveToFile(String filePath) throws FileNotFoundException, AccessDeniedException, SerializationException {
    XMLDumper xmlDumper = new XMLDumper(filePath, this);
    xmlDumper.dump();
    hasUnsavedChanges = false;
  }

  public void loadFromFile(String filePath) throws ParserConfigurationException, SAXException, IOException, Exception {
    XMLDumper xmlDumper = new XMLDumper(filePath, this);
    clear();
    xmlDumper.load();
    hasUnsavedChanges = false;
  }

  public void add(LabWork labWork) {
    collection.add(labWork);
    setUpdatedAt();
  }

  public LabWork getById(long id) {
    for (LabWork labWork : collection) {
      if (labWork.getId() == id) {
        return labWork;
      }
    }
    return null;
  }

  public ArrayList<LabWork> getList() {
    return new ArrayList<>(collection);
  }

  public boolean updateById(long id, LabWork newLabWork) {
    for (LabWork labWork : collection) {
      if (labWork.getId() == id) {
        collection.remove(labWork);
        collection.add(newLabWork);
        setUpdatedAt();
        return true;
      }
    }
    return false;
  }

  public void clear() {
    collection.clear();
    createdAt = LocalDateTime.now();
    setUpdatedAt();
    lastId = 1;
  }

  public boolean removeById(long id) {
    Iterator<LabWork> iterator = collection.iterator();
    while (iterator.hasNext()) {
      LabWork labWork = iterator.next();
      if (labWork.getId() == id) {
        iterator.remove();
        setUpdatedAt();
        return true;
      }
    }
    return false;
  }

  public boolean getHasUnsavedChanges() {
    return hasUnsavedChanges;
  }
}
