import { createContext, ReactNode, useEffect, useMemo, useState } from 'react';
import { loadAccessToken, removeClientAccessToken } from '@/api/axios';
import { useGetAuthStatus } from '@/hooks';

export const AuthContext = createContext();

export function AuthProvider({ children }) {
  const { data, isLoading } = useGetAuthStatus();
  const [auth, setAuth] = useState({
    isAuthenticated: false,
    userId: null,
    roles: [],
  });

  useEffect(() => {
    console.log("Auth status change")
    console.log(data)
    if (data) {
      setAuth({
        isAuthenticated: data.isAuthenticated,
        userId: data.userId,
        roles: data.roles || [],
      });
    }
  }, [data, isLoading]);

  const value = useMemo(() => ({
    ...auth,
    isLoading: isLoading,
    setAuth,
  }), [auth, isLoading]);

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}
