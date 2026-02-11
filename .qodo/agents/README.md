# AI Agent System - Todo-Based Agent Management

## Overview

This directory contains AI agent configurations and decision-making frameworks that can be exported and reused across projects. The system uses a todo-based approach for agent orchestration and decision tracking.

## Agent Structure

```
.qodo/agents/
├── README.md                 # This file - system overview
├── migration-agent.json       # Migration strategies and patterns
├── architecture-agent.json    # Architecture analysis configurations
├── problem-solver.json        # Problem resolution frameworks
├── decision-tree.json         # Decision-making trees
├── export/                   # Exported agent packages
│   ├── spring-boot-migration.json
│   ├── architecture-analysis.json
│   └── problem-resolution.json
└── templates/                 # Reusable agent templates
    ├── migration-template.json
    ├── analysis-template.json
    └── solver-template.json
```

## Agent Configuration Files

### 1. Migration Agent Configuration
```json
{
  "agentType": "migration",
  "name": "Framework Migration Agent",
  "version": "1.0.0",
  "capabilities": [
    "dependency-analysis",
    "breaking-change-detection",
    "version-compatibility-check",
    "incremental-migration",
    "test-validation"
  ],
  "strategies": {
    "spring-boot": {
      "phases": [
        {
          "name": "assessment",
          "steps": ["version-detection", "dependency-analysis", "breaking-change-identification"]
        },
        {
          "name": "preparation", 
          "steps": ["backup-current", "update-dependencies", "add-compatibility-layers"]
        },
        {
          "name": "migration",
          "steps": ["update-framework", "fix-breaking-changes", "update-imports"]
        },
        {
          "name": "validation",
          "steps": ["compilation-test", "unit-tests", "integration-tests", "functional-tests"]
        }
      ],
      "commonIssues": [
        {
          "problem": "jaxb-namespace-changes",
          "solution": "add-jakarta-dependencies",
          "fallback": "compatibility-layer"
        },
        {
          "problem": "jackson-version-upgrade",
          "solution": "spring-boot-jackson2-module",
          "fallback": "explicit-version-override"
        }
      ]
    }
  }
}
```

### 2. Architecture Agent Configuration
```json
{
  "agentType": "architecture",
  "name": "Architecture Analysis Agent", 
  "version": "1.0.0",
  "capabilities": [
    "layer-identification",
    "component-mapping",
    "relationship-analysis",
    "diagram-generation",
    "pattern-recognition"
  ],
  "analysisTypes": {
    "clean-architecture": {
      "layers": ["domain", "application", "infrastructure"],
      "principles": ["solid", "hexagonal-ports", "dependency-inversion"],
      "outputs": ["uml-diagram", "component-diagram", "package-structure"]
    },
    "simplification": {
      "removeRedundancy": true,
      "focusOnEssentials": true,
      "maintainDataFlow": true
    }
  }
}
```

### 3. Problem Solver Configuration
```json
{
  "agentType": "problem-solver",
  "name": "Problem Resolution Agent",
  "version": "1.0.0", 
  "capabilities": [
    "root-cause-analysis",
    "hypothesis-testing",
    "incremental-isolation",
    "parallel-debugging",
    "solution-validation"
  ],
  "resolutionStrategies": {
    "compilation-errors": {
      "approach": "systematic-isolation",
      "techniques": ["dependency-tree-analysis", "classpath-inspection", "version-conflict-detection"]
    },
    "test-failures": {
      "approach": "test-driven-debugging",
      "techniques": ["single-test-isolation", "log-analysis", "mock-verification"]
    },
    "runtime-errors": {
      "approach": "context-analysis",
      "techniques": ["configuration-review", "environment-check", "dependency-verification"]
    }
  }
}
```

## Decision Tree Framework

### Decision Making Structure
```json
{
  "decisionFramework": {
    "version": "1.0.0",
    "decisionTypes": [
      "technical-choices",
      "architecture-decisions", 
      "problem-resolution",
      "migration-strategy"
    ],
    "process": {
      "context-analysis": "gather-project-context",
      "option-generation": "identify-possible-solutions",
      "evaluation": "assess-options-against-criteria",
      "selection": "choose-optimal-solution",
      "implementation": "execute-with-monitoring",
      "validation": "verify-solution-effectiveness"
    }
  }
}
```

## Export/Import System

### Export Format
```json
{
  "exportPackage": {
    "metadata": {
      "agentVersion": "1.0.0",
      "exportDate": "2026-02-11T11:39:00Z",
      "projectType": "spring-boot",
      "compatibility": ["spring-boot-3.x", "spring-boot-4.x"]
    },
    "agents": [
      {
        "type": "migration",
        "config": "migration-agent.json",
        "successRate": 0.95,
        "usageCount": 15
      },
      {
        "type": "architecture", 
        "config": "architecture-agent.json",
        "successRate": 0.98,
        "usageCount": 8
      }
    ],
    "knowledgeBase": {
      "resolvedIssues": 47,
      "patterns": ["incremental-migration", "compatibility-layer", "test-driven-validation"],
      "bestPractices": ["parallel-tool-usage", "context-memory", "structured-logging"]
    }
  }
}
```

### Import Process
```bash
# AI Agent reads this structure to make decisions:
ai-agent --import .qodo/agents/ \
         --project-type spring-boot \
         --context current-project-state

# Agent applies learned patterns:
ai-agent --apply-pattern migration-agent \
         --strategy incremental-migration \
         --target-version 4.0.2
```

## Usage in Other Projects

### 1. Copy Agent Configuration
```bash
# Copy agent configurations to new project
cp -r .qodo/agents/ /path/to/new-project/.qodo/agents/

# Export specific agent for current project
ai-agent --export migration-agent \
         --output /path/to/new-project/.qodo/agents/export/spring-boot-migration.json
```

### 2. AI Agent Integration
```java
// AI Agent reads configuration for decision making
public class ProjectAwareAgent {
    
    public AgentDecision makeDecision(ProjectContext context) {
        AgentConfig config = loadAgentConfig(".qodo/agents/");
        
        DecisionTree tree = config.getDecisionTree(context.getProjectType());
        return tree.evaluate(context);
    }
    
    private AgentConfig loadAgentConfig(String path) {
        // Load all agent configurations
        List<AgentConfig> agents = Arrays.asList(
            loadConfig(path + "migration-agent.json"),
            loadConfig(path + "architecture-agent.json"),
            loadConfig(path + "problem-solver.json")
        );
        
        return AgentConfig.combine(agents);
    }
}
```

### 3. Learning and Adaptation
```json
{
  "learningSystem": {
    "captureDecisions": true,
    "trackOutcomes": true,
    "updateSuccessRates": true,
    "adaptPatterns": true,
    "shareKnowledge": true
  },
  "adaptation": {
    "fromFailures": "update-strategies-based-on-errors",
    "fromSuccess": "reinforce-successful-patterns", 
    "fromContext": "adapt-to-project-specifics"
  }
}
```

## Integration with AI Systems

### AI Decision Making Process
1. **Context Loading**: AI reads `.qodo/agents/` configurations
2. **Pattern Matching**: Matches current problem to known patterns
3. **Strategy Selection**: Chooses optimal approach based on success rates
4. **Execution**: Applies pattern with project-specific adaptations
5. **Learning**: Updates success rates and refines patterns

### Benefits for AI Systems
- **Consistent Decision Making**: Standardized approaches across projects
- **Knowledge Accumulation**: Learning from previous executions
- **Pattern Reuse**: Proven strategies applied consistently
- **Context Awareness**: Adaptations based on project specifics
- **Quality Assurance**: Validated patterns with known success rates

This system enables AI agents to make informed, context-aware decisions based on accumulated experience and proven patterns.
