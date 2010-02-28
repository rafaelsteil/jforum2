# @version: $Id: mysql_40.sql,v 1.1 2005/09/28 09:47:15 vmal Exp $
# ################
# PermissionControl
# ################
PermissionControl.deleteAllRoleValues = DELETE jforum_role_values \
	FROM jforum_role_values rv, jforum_roles r \
	WHERE r.role_id = rv.role_id \
	AND r.group_id = ?
