package sadupstaff.exception.section;

public class DeleteSectionException extends RuntimeException{
    public DeleteSectionException(Integer number) {
        super(String.format("%d имеет сотрудников, удаление запрещено", number));
    }
}
