package sadupstaff.exception.documentgeneration;

public class IncorrectNAMEFormatException extends RuntimeException {
    public IncorrectNAMEFormatException(String JudgeName, String JudgeOrganizerName, String ConcordantName) {

        super(String.format(
                "Cудья: %s; " +
                "Судья организатор: %s; " +
                "Сотрудник согласующего отдела: %s; " +
                "У судьи, судьи организотора или сотрудника согласующего отдела неверный формат ФИО " +
                "Пример верного формата: И.И. Иванов",
                JudgeName, JudgeOrganizerName, ConcordantName));
    }
}
