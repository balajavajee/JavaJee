/*package com.bala.accesscontrol.filter;
@Component
@Order(1)
public class TransactionFilter implements Filter {
 
    @Override
    public void doFilter
      ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
  
        HttpServletRequest req = (HttpServletRequest) request;
        LOG.info(
          "Starting a transaction for req : {}", 
          req.getRequestURI());
  
        chain.doFilter(request, response);
        LOG.info(
          "Committing a transaction for req : {}", 
          req.getRequestURI());
    }
 
    // other methods 
}*/