# ################
# PermissionControl
# ################
PermissionControl.deleteRoleValuesByRoleId = DELETE FROM jforum_role_values WHERE role_id IN (?)

PermissionControl.getRoleIdsByGroup = SELECT DISTINCT rv.role_id \
	FROM jforum_role_values rv, jforum_roles r \
	WHERE r.role_id = rv.role_id \
	AND r.group_id = ?
	
PermissionControl.getRoles = SELECT r.name, '0' AS role_value FROM jforum_roles r WHERE r.group_id IN (#IN#)

PermissionControl.getRoleValues = SELECT r.name, rv.role_value \
	FROM jforum_roles r, jforum_role_values rv \
	WHERE r.role_id = rv.role_id  \
	AND r.group_id IN (#IN#) \
	ORDER BY name
