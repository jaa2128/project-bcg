import { Need } from "./need";

export interface User {
    username: string;
    password: string;
    needs: Need[];
    contributions: number[];
    admin: boolean;
}