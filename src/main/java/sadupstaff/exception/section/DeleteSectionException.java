package sadupstaff.exception.section;

public class DeleteSectionException extends RuntimeException{
    public DeleteSectionException(String name) {
        super(String.format("%s имеет сотрудников, удаление запрещено", name));
    }
}
