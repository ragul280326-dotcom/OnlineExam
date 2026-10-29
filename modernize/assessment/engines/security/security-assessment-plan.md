> **Important:** Do NOT call `#appmod-run-assessment-action` or `#appmod-run-assessment-report` — Only use `#appmod-cwe-rules-assessment` and `#appmod-cve-assessment` as instructed below.

# Task

You are a [code security reviewer], NOT an implementation developer.
Your task is to assess applications for security vulnerabilities in two parts:
  - **Part 1**: CVE (Common Vulnerabilities and Exposures) dependency vulnerability assessment
  - **Part 2**: CWE (Common Weakness Enumeration) code-level vulnerability assessment
You MUST follow [Instructions](#Instructions) to perform both parts of the assessment.

# Context

- Checklist directory: "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cweChecklist"
- Each category's rules are pre-written to: "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cweChecklist/{sanitized-category-name}.json"
  (e.g., category "Injection Attacks" → "injection-attacks.json", "Concurrency & Synchronization" → "concurrency-synchronization.json")
- CWE Rules to process: "59"
- You MUST track progress using todo list and the per-category checklist files

# Instructions

Execute the following steps in order. There are only 3 steps total.

## Step 1: Read CWE Checklist (Initialization)
- Read all JSON files from "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cweChecklist" — each file is named after a sanitized CWE category and contains that category's rules with status "PENDING"
- This gives you the complete list of 59 CWE rules grouped by category
- Record each file name and the CWE-IDs it contains — you will need these for Step 2

## Step 2: Launch ALL Subagents AT ONCE (CVE + CWE)

**[CRITICAL]: You MUST launch EVERY subagent listed below in a SINGLE message with MULTIPLE simultaneous tool calls.**
**Do NOT launch the CVE subagent first and then CWE subagents, or vice versa — ALL must start at the same time.**

### Step 2a: CVE Subagent (1 subagent) — Extract Direct Dependencies and Call CVE Assessment Tool

Launch ONE SEPARATE modernize-java-assessment agent as subagent with this prompt:
> **Your task**: Extract direct dependencies and check them for CVE vulnerabilities.
>
> **Step A — Extract Direct Dependencies Only**:
> - **Wrapper preference**: Use Maven Wrapper (`./mvnw` or `./mvnw.cmd` on Windows) or Gradle Wrapper (`./gradlew` or `./gradlew.bat` on Windows) when present in the project root; fall back to `mvn`/`gradle` otherwise. **Important**: Always use `./` prefix — bare `mvnw` will fail in PowerShell.
> - For Maven projects: run `./mvnw dependency:list -DoutputAbsoluteArtifactId=true -DexcludeTransitive=true` (or `mvn` if no wrapper) in the workspace root to resolve direct dependencies
>   - Parse the output to collect GAV coordinates for direct dependency
>   - If the Maven command fails, fall back to static parsing of `pom.xml` files (direct dependencies only). Extract only what is explicitly declared — do NOT use the web to look up managed versions from BOMs or parent POMs. Proceed immediately to Step B with whatever you collected.
> - For Gradle projects: run `./gradlew dependencies --configuration runtimeClasspath` (or `gradle` if no wrapper) or parse `build.gradle`/`build.gradle.kts` files as fallback
> - For each dependency, collect:
>   - **Coordinate**: `groupId:artifactId:version` (Maven GAV format)
>   - **File path**: workspace-relative path to the build file that declares or inherits the dependency (e.g., `module-a/pom.xml`)
>   - **Line number**: 1-based line number of the `<dependency>` declaration if directly declared, or 0 if transitive
>
> **Step B — Call CVE Assessment Tool**:
> - Call the #appmod-cve-assessment tool with:
>   - `cveResultFilePath`: "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cve_assessment_result.json"
>   - `ecosystem`: "maven"
>   - `dependencies`: array of dependency coordinate strings
>   - `dependencyLocations`: array of `{ coordinate, filePath, lineNumber }` objects for each dependency
>
> **[CRITICAL CONSTRAINTS]**:
> - You MUST call #appmod-cve-assessment regardless of how you obtained the dependencies (build tool command OR fallback static parsing). This tool writes the result file that unblocks the pipeline.
> - Do NOT use the `web` tool for ANY part of this task — not for looking up CVE/vulnerability information, not for resolving dependency versions, not for fetching BOMs or package metadata. All CVE lookups are handled exclusively by #appmod-cve-assessment.
> - If the build tool command fails, extract only what is explicitly declared in the build files and proceed immediately to Step B — even if some versions are missing or incomplete.
> - If you found zero dependencies, still call #appmod-cve-assessment with an empty array so the result file is created.

### Step 2b: CWE Rules Assessment Subagents (one per category file)

- **[IMPORTANT]: Each category file is one batch. Launch ALL CWE subagents SIMULTANEOUSLY—one per category file**
- **[IMPORTANT]: Do NOT wait for one batch to finish before starting the next—all CWE subagents must run IN PARALLEL**
- Each JSON file in "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cweChecklist" is one batch (one category)
- Prepare ALL subagents before launching any

For EACH category JSON file, launch a SEPARATE modernize-java-assessment agent as subagent with this prompt:
> Run #appmod-cwe-rules-assessment tool with CWE-IDs: [all CWE-IDs from this category file] (e.g., "CWE-79, CWE-89, CWE-78").
> Then immediately overwrite your assigned category file (e.g., "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cweChecklist/injection-attacks.json") with the updated results.
> **[CRITICAL] This file belongs ONLY to you—no other subagent will touch it, so no conflict handling is needed:**
> 1. Write a JSON array with EVERY rule from your category file
> 2. Set each rule's "status" to "FOUND" or "NOT_FOUND"
> 3. Set each rule's "evidence.files" to the returned array (workspace-relative paths for FOUND; `[]` for NOT_FOUND)
> 4. Set each rule's "evidence.explanation" to the returned string (class/method/line detail for FOUND; `""` for NOT_FOUND)
> 5. Write the file ONCE and stop—do NOT re-read or re-write again
> - **[CRITICAL]: Do NOT skip any rule—every rule in your category file must be updated**

### Wait for All Subagents
- Wait until EVERY subagent (CVE + all CWE) has finished
- Do NOT touch any category file yourself—each subagent owns the updates for its own file

## Step 3: Verify Completion
- Re-read all JSON files from "c:\Users\USER\Downloads\online_examination_system_vscode\.github\modernize\assessment\engines\security\cweChecklist"
- If ANY rules still have status "PENDING", return to Step 2b and re-launch subagents ONLY for those incomplete category files
- **[IMPORTANT]: Do NOT finish until ALL "59" rules have status "FOUND" or "NOT_FOUND"**
