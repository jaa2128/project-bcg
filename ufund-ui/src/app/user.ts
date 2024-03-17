import { Need } from "./need";

export interface User {
    username: string;
    password: string;
    needs: number[];
    contributions: number[];
    admin: boolean;
}