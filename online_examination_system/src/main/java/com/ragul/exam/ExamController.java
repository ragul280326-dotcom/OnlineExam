package com.ragul.exam;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class ExamController {
    private final List<Question> questions = List.of(
        new Question(1, "Which keyword is used to inherit a class in Java?", new String[]{"implements", "extends", "inherits", "super"}, 1),
        new Question(2, "Which method is the entry point of a Java application?", new String[]{"start()", "run()", "main()", "init()"}, 2),
        new Question(3, "Which collection does not allow duplicate elements?", new String[]{"List", "ArrayList", "Set", "Queue"}, 2),
        new Question(4, "Which SQL command retrieves data?", new String[]{"GET", "SELECT", "OPEN", "FETCH ALL"}, 1),
        new Question(5, "What does HTML stand for?", new String[]{"Hyper Trainer Marking Language", "HyperText Markup Language", "HighText Machine Language", "Hyper Tool Multi Language"}, 1),
        new Question(6, "Which Spring annotation marks a REST controller?", new String[]{"@Service", "@Repository", "@RestController", "@Entity"}, 2),
        new Question(7, "Which symbol ends a typical Java statement?", new String[]{":", ".", ";", ","}, 2),
        new Question(8, "Which data type stores true or false?", new String[]{"int", "String", "boolean", "char"}, 2),
        new Question(9, "Which CSS property changes text colour?", new String[]{"font-style", "color", "background-image", "text-weight"}, 1),
        new Question(10, "Which command compiles Java source code?", new String[]{"java", "javac", "javadoc", "jar"}, 1)
    );

    @GetMapping("/")
    public String home() { return "index"; }

    @PostMapping("/start")
    public String start(@RequestParam String studentName, HttpSession session, Model model) {
        if (studentName == null || studentName.trim().isEmpty()) {
            model.addAttribute("error", "Please enter your name.");
            return "index";
        }
        session.setAttribute("studentName", studentName.trim());
        session.removeAttribute("lastResult");
        model.addAttribute("studentName", studentName.trim());
        model.addAttribute("questions", questions);
        return "exam";
    }

    @PostMapping("/submit")
    public String submit(@RequestParam Map<String, String> form, HttpSession session, Model model) {
        Object nameObj = session.getAttribute("studentName");
        if (nameObj == null) return "redirect:/";
        int score = 0;
        List<Map<String, Object>> review = new ArrayList<>();
        for (Question q : questions) {
            String selected = form.get("q" + q.getId());
            int chosen = -1;
            try { if (selected != null) chosen = Integer.parseInt(selected); } catch (NumberFormatException ignored) {}
            boolean correct = chosen == q.getAnswer();
            if (correct) score++;
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("question", q.getText());
            item.put("selected", chosen >= 0 && chosen < q.getOptions().length ? q.getOptions()[chosen] : "Not answered");
            item.put("correctAnswer", q.getOptions()[q.getAnswer()]);
            item.put("correct", correct);
            review.add(item);
        }
        model.addAttribute("studentName", nameObj);
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute("percentage", score * 100 / questions.size());
        model.addAttribute("review", review);
        session.setAttribute("lastResult", score);
        return "result";
    }
}


