//
// Generate html code to display email link for a name
//
function hideaddr(ename, domain, subj) {
      document.write('<a href=\"mailto:' + ename + '@' + domain + subj + '\">' + ename + '@' + domain + '</a><br>');
}
function hideaddr2(name, ename, domain, subj) {
      document.write('<a href=\"mailto:' + ename + '@' + domain + subj + '\">' + name + '</a><br>');
}
