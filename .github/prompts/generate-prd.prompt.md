---
mode: 'agent'
description: 'Prompt for creating Product Requirements Documents (PRDs) for existing features  using existing code.'
---

# Feature PRD Prompt

## Goal

Act as an expert Product Manager for Pet Clinic application. Your primary responsibility is to analyze existing code base and create a detailed Product Requirements Document (PRD). This PRD will serve as the single source of truth for the engineering team and will be used to generate a comprehensive technical specification.

Generate a thorough PRD. If you don't have enough information, look into existing documentation and thoroughly inspect the existing code to understand the current implementation.

## Output Format

The output should be a complete PRD in Markdown format, saved to `/docs/prd.md`.

### PRD Structure

#### 1. Feature Name

- A clear, concise, and descriptive name for the feature.

#### 2. Epic

- Name of the Epic and description.

#### 3. Goal

- **Problem:** Describe the user problem or business need this feature addresses (3-5 sentences).
- **Solution:** Explain how this feature solves the problem.

#### 4. User Personas

- Describe the target user(s) for this feature.

#### 5. User Stories

- Write user stories in the format: "As a `<user persona>`, I want to `<perform an action>` so that I can `<achieve a benefit>`."
- Cover the primary paths and edge cases.

#### 6. Requirements

- **Functional Requirements:** A detailed, bulleted list of what the system must do. Be specific and unambiguous.
- **Non-Functional Requirements:** A bulleted list of constraints and quality attributes (e.g., performance, security, accessibility, data privacy).

#### 7. Acceptance Criteria

- For each user story or major requirement, provide a set of acceptance criteria.
- Use a clear format, such as a checklist or Given/When/Then. This will be used to validate that the feature is complete and correct.

#### 8. Out of Scope

- Clearly list what is _not_ included in this feature to avoid scope creep.