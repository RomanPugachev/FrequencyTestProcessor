package org.example.frequencytestsprocessor.services.idManagement;

import org.example.frequencytestsprocessor.MainController;

import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class IdManager {
    private final MainController mainController;
    private final List<HasId> slaves = new ArrayList<>();
    private final List<Predicate<String>> validators = new ArrayList<>();
    private final List<String> forbiddenChars = new ArrayList<String>();

    {
        Collections.addAll(forbiddenChars,"?", "-", "+", "/", "*", "=", "(", ")");
        initializeValdatationConditions();
    }

    private void initializeValdatationConditions() {
        Predicate<String> notEmptyNotNull = id -> id != null && !id.isEmpty();
        Predicate<String> notContainsForbiddenChars = id -> !forbiddenChars.stream().anyMatch(id::contains);
        Predicate<String> startsWithLetter = id -> Character.isLetter(id.charAt(0));
        Predicate<String> notContainsSpaces = id -> !id.contains(" ");
        Collections.addAll(validators, notEmptyNotNull, notContainsForbiddenChars, startsWithLetter, notContainsSpaces);
    }

    public IdManager(MainController mainController) {
        this.mainController = mainController;
    }

    public HasId addSlave(HasId slave) {
        slaves.add(slave);
        refreshOneId(slave);
        return slave;
    }

    public interface HasId {
        String getId();
        void setId(String id);
    }

    public void refreshOneId(HasId slave) {
        if (validateId(slave)) return;
        List<String> existingValidIds = new ArrayList<>(slaves.size());
        slaves.forEach(cur -> {
            if (validateId(cur)){
                existingValidIds.add(cur.getId());
            }
        });
        slave.setId(generateId(existingValidIds));
    }
    /* Main function for managing all ids at in one call */
    public void refreshAllIds() {
        List<String> existingValidIds = new ArrayList<>(slaves.size());
        List<HasId> slavesToGetNewId = new ArrayList<>(slaves.size());
        slaves.forEach(slave -> {
            if (validateId(slave)){
                existingValidIds.add(slave.getId());
            } else slavesToGetNewId.add(slave);
        });
        slavesToGetNewId.forEach(slave -> {
            var newId = generateId(existingValidIds);
            slave.setId(newId);
            existingValidIds.add(newId);
        });
    }
    
    /* Function for generation id for one slave */
    private String generateId (List<String> existingValidIds) {
        long minId = Long.MAX_VALUE;
        long maxId = Long.MIN_VALUE;
        Long numRows = (long) existingValidIds.size();
        if (numRows.equals(0L)) return "F0";
        Set<Long> existingNums = new HashSet<>();
        Pattern regexPattern = Pattern.compile("^F\\d+$");
        // Searching for existing indexes
        for (String s : existingValidIds) {
            if (regexPattern.matcher(s).matches()) {
                Long curNum = Long.parseLong(s.substring(1));
                existingNums.add(curNum);
                minId = Math.min(minId, curNum);
                maxId = Math.max(maxId, curNum);
            }
        }
        // Generating new index
        if (minId > 0L) {
            return "F0";
        } else {
            Long newId = minId + 1L;
            while (existingNums.contains(newId)) {
                newId++;
            }
            return "F" + newId;
        }
    }

    private boolean validateId(HasId slave) {
        return validators.stream().allMatch(validator -> validator.test(slave.getId()));
    }

    public void removeSlave(HasId slave) {
        slaves.remove(slave);
    }

    public void removeSlaves(Collection<HasId> slaves) {
        this.slaves.removeAll(slaves);
    }
}
