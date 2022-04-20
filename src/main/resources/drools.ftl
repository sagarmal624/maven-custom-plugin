${droolsFile.globalContent}

<#list droolsFile.rules as rule>
rule "${rule.ruleName}"
    when
      ${rule.ruleCondition}
    then
    ${rule.ruleAction}
end;
</#list>
